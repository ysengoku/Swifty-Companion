# Technical Notes

## Common

### App basics

- `AndroidManifest`:  
  Declares an app's components, permissions, and configuration, so that the Android system knows what the app contains and what it's allowed to do.
- `MainActivity`:  
  The app's entry point (an `Activity`, Android's system-managed screen container). Hosts the Compose UI and navigation graph.

### Kotlin class types

- `data class`:  
  Classes that hold only data, nothing else. It auto-generates `equals`, `hashCode`, `toString`, and `copy` from its properties, so that doesn't need to be written by hand.
- `object`:  
  Used when only one instance is ever needed, like a shared API client or config holder, without writing a singleton pattern by hand.
- `companion object`:  
  A singleton tied to a class, accessible without an instance. Used here to hold `ViewModelProvider.Factory`, since `DetailViewModel` needs constructor arguments that the default creation can't supply.
- `interface`:  
  A contract of methods and properties that a class can implement, so callers can depend on the contract instead of a specific implementation.
- `sealed interface`:  
  An interface whose implementations are all known at compile time. Used so a `when` over them can be checked exhaustively, catching a missing case at compile time instead of at runtime.
- `repository`:  
  A class that hides a data source behind a simple API, so the ViewModel doesn't depend on network details directly.


### Variables

- `val`:  
  Immutable, read-only local variables that can't be reassigned a different value after initialization.
- `var`:  
  Mutable variables. Used only when a value needs to change after creation, like local UI state.

### Kotlin language features

- `Result<T>`:  
  A type that wraps either a success value or a failure, without throwing. Lets `fetchUser` report failure so the ViewModel can handle it explicitly instead of with try/catch.
- `suspend fun`:  
  A function that can pause and resume without blocking the thread. Used for network calls so that they don't block the UI while waiting for a response.
- `by`:  
  Delegates a property's get/set to another object. Used with `remember { mutableStateOf(...) }` so taht the underlying state holder can be read and written like a plain variable.

## Data layer

### Networking libraries

- `okhttp3 / retrofit2`:  
  HTTP client and type-safe REST client libraries. Android has no built-in HTTP client suited for this, so these handle networking, threading, and JSON mapping.
- `Interceptor`:  
  Code that observes or modifies every HTTP request and response. Used to attach the OAuth2 bearer token and retry once on a 401/403, in one place instead of at every call site.
- `Gson`:  
  A library that converts between JSON and Kotlin objects. Maps the 42 API's JSON directly onto data classes without manual parsing.

### Annotations

- `annotation`:  
  Metadata attached to code that tools or libraries read at compile time or runtime, so behavior can be declared instead of written by hand.
- `@SerializedName`:  
  Maps a JSON key to a differently named Kotlin property, so a property can stay idiomatic camelCase while matching the API's snake_case key.
- `@GET / @Path`:  
  Retrofit annotations that declare an HTTP route and its path parameters, so Retrofit generates the network call instead of it being written by hand.
- `@Synchronized`:  
  Ensures only one thread executes the function at a time. Used so two threads can't read or refresh the cached token at once.

### Builder pattern

- `Builder`:  
  A pattern that constructs a complex object step by step. Used for objects like `OkHttpClient` or `Request` that have many optional settings, instead of one large constructor.

### Labeled returns

- `return@Interceptor`:  
  A labeled return that exits the interceptor lambda specifically. Needed because a plain `return` isn't allowed inside a lambda that isn't a function itself.

### Resource handling & testability

- `use { }`:  
  Runs a block on a `Closeable` and closes it automatically afterward, even if an exception is thrown while reading it.
- `Clock`:  
  A source of the current time that can be swapped for a fixed one in tests, so token expiry can be tested without waiting for real time to pass.

## UI layer

### Compose basics

- `Composable`:  
  A function that describes part of the UI. Marking it lets Compose call it again (recompose) automatically when its inputs change.
- `Modifier`:  
  Chainable configuration for size, padding, layout, and behavior. Lets these concerns be composed and passed down without every composable needing its own parameters for each one.
- `-> Unit`:  
  A function type with no return value. Used for callback parameters like `onSubmit`, since the caller only needs to be notified, not to get a value back.

### State management

- `remember / mutableStateOf`:  
  Holds UI state across recompositions. Tells Compose a value changed, so views depending on it recompose.
- `rememberSaveable`:  
  Like `remember`, but survives configuration changes like screen rotation, so state (like whether a section is expanded) isn't lost.
- `collectAsState()`:  
  Converts a `StateFlow` into Compose `State`, so the UI recomposes automatically when the ViewModel's state changes.

### Compose components

- `Scaffold`:  
  A layout composable that provides a standardized structure following Material Design. It automatically arranges key screen elements, such as the top bar, bottom bar, and content, so padding and insets don't need to be handled by hand.
- `AsyncImage`:  
  Loads and displays an image from a URL asynchronously, handling background loading, caching, and cancellation without writing it manually.
- `LazyColumn / LazyListScope`:  
  A vertically scrolling list that only composes items currently on screen, so long lists don't render everything at once.


### Navigation

- `NavHost / composable / rememberNavController`:  
  Declares the app's screens and how to move between them (`search` -> `detail/{login}`), instead of managing a back stack by hand.

### ViewModel creation

- `Factory`:  
  Creates a `ViewModel` with constructor arguments that the default, no-argument creation can't supply.
- `ViewModel`:  
  Holds UI state and survives configuration changes independently of the UI, so navigating away and back doesn't lose data or refetch unnecessarily.
