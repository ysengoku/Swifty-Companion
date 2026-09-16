# 42 API Notes

In this project, 2 endpoints from 42 API are used:
- `/oauth/token`
- `/v2/users/:login`

## Oauth Token

This project uses the `client_credentials` grant type (UID/Secret from `.env`). This OAuth 2.0 grant type is for the client acting on its own behalf, not a specific user's ("by allowing the third-party application to obtain access on its own behalf"). It fits here since the app only ever reads public profile data, nothing tied to a signed-in user's account.   

**In this app:**   
- `TokenManager` caches the token in memory and reuses it across requests, refetching only once it's within 60s of `expires_in`,  avoiding to hit `/oauth/token` on every API call.
- `IntraApi`'s `authInterceptor` attaches `Authorization: Bearer <token>` to every request automatically, so call sites (`UserRepository`, `IntraService`) never touch tokens directly.
- On a `401`/`403`, the interceptor invalidates the cached token, fetches a fresh one, and retries the request once.
- A token request failure surfaces as `TokenException`, turned into an error response rather than thrown, so a dead token doesn't crash the app.

**Manual check outside the app:**

```js
const res = await fetch("https://api.intra.42.fr/oauth/token", {
  method: "POST",
  headers: { "Content-Type": "application/x-www-form-urlencoded" },
  body: new URLSearchParams({
    grant_type: "client_credentials",
    client_id: "your_uid",
    client_secret: "your_secret",
  }),
});
const token = await res.json();

token

// The response includes { access_token, token_type, expires_in, created_at... }
// expires_in is in seconds (2 hours as of writing).
```

## User information

Notes on `GET /v2/users/:login`, the endpoint `UserProfileScreen` is built on.

### Response

A single call returns the user's full profile plus everything needed for this view. No follow-up requests needed:

- `campus` / `campus_users`: all campuses the user belongs to; `campus_users.is_primary` marks the current one
- `titles` / `titles_users`: all titles earned; `titles_users.selected` marks the one currently displayed
- `cursus_users`: every cursus the user is enrolled in, each with its own nested `skills` list and `level`
- `projects_users`: every project the user has done, across all cursus

### Pagination: the thing to watch for

`/v2/users/:login` returns `projects_users` (and the other nested lists) **in full**, regardless of how many projects the user has.

This is *not* true of the equivalent standalone endpoints. `GET /v2/users/:id/projects_users` is a paginated collection endpoint, capped at 30 items per page by default, navigated with the `page[number]` parameter. Fetching a user's complete project list that way means looping over pages until an empty one comes back.

**Practical takeaway:** since this app needs *all* of a user's cursus and projects, fetching them via `/v2/users/:login` avoids pagination entirely. Going through the dedicated sub-resource endpoints instead would require handling pagination for no benefit here.

**Manual check outside the app:**

```js
// Get token
const res = await fetch("https://api.intra.42.fr/oauth/token", {
  method: "POST",
  headers: { "Content-Type": "application/x-www-form-urlencoded" },
  body: new URLSearchParams({
    grant_type: "client_credentials",
    client_id: "your_uid",
    client_secret: "your_secret",
  }),
});
const token = await res.json();

const login = <<login_to_search>>
const user = await fetch("https://api.intra.42.fr/v2/users/" + login, {
  headers: { Authorization: "Bearer " + token },
}).then(r => r.json());

user.projects_users

// projects_users endpoint to compare — page 1
const projectsPage1 = await fetch("https://api.intra.42.fr/v2/users/" + user.id + "/projects_users?page=1", {
  headers: { Authorization: "Bearer " + token },
}).then(r => r.json());
projectsPage1.length // 30

// page 2
const projectsPage2 = await fetch("https://api.intra.42.fr/v2/users/" + user.id + "/projects_users?page=2", {
  headers: { Authorization: "Bearer " + token },
}).then(r => r.json());
projectsPage2.length // remainder, or 0 if the user has ≤30 projects
```
