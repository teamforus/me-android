
# Android App **"Me"**

This app is an open-source project to offer citizens a way of managing
their identity and their personal data by the
[Forus Foundation](http://forus.io).

Until the release, the app can be used as an experimental
personal Ether-wallet.

## Structure

### Packages

The app is structured in packages with their dependencies.

#### Dao

This package contains all the database abstraction objects, to get the
entities from the room database.

##### Dependencies

Required by: primarely services, which offer easy, threadsafe interfaces
per dao (e.g. RecordService provides a threadsafe interaction with the
RecordDao). Can also be used directly throughout the app

#### Entities

This package contains all the entities (data objects) which the user
can interact with.

#### External

Stand-alone package. This package contains the interactions called upon
by other applications (e.g. the login-process of other apps requiring
the users identity.

##### Dependencies

No other package may depend on the external package.

#### Helpers

The package contains the methods and classes to reduce code throughout
the app. E.g. if a certain url-modification is necessary throughout the
app, make the code for the modification in a helper-companion class
method. This package can also offer interactions with plugins, such as
the QR-code generator plugin.

##### Dependencies

This package may not depend on any io.forus.me package, other than the
Entities package.

#### Services

This package provides data from outside datasources, such as the Forus
API or the (or any) Ethereum Blockchain, but also offers an easy,
threadsafe interaction with the entity-dao's.

_Note: This package and all of its contents should rename "Service" to "Provider"_

##### Dependencies

This app depends on the Dao package for local Room database-interactions.
Any package in the app (other than the Helper package) may depend on
this package and this is encouraged (rather than making a custom
implementation of gathering data)

#### Views

This package contains implementation of views other than activities.

##### Dependencies

No package may depend on this package.

#### Web3

This package contains the implementations of Ethereum interactions.
Only the services package and the "sync" methods of EthereumItem-
implementation may depend on this package.