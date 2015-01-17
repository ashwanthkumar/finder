[![Build Status](https://snap-ci.com/ashwanthkumar/finder/branch/master/build_image)](https://snap-ci.com/ashwanthkumar/finder/branch/master)

# finder
Finder is a hobby project to build a [Wayback](https://github.com/iipc/openwayback/) clone without converting our existing crawled data which are in SequenceFiles to WARC / ARC formats. Architecture is very much inspired from Wayback but with a few changes. It can not only support HTML pages but finder can provide key+timestamp based access to any dataset.

## Getting Started
* Run the `SampleDatagenerator` program from your fav. IDE
* By default it would create a folder in the project structure called "example-user-data"
* Run the `FinderService` from `services` module
* Example Queries
  * [http://localhost:7070/user/search/Tanek](http://localhost:7070/user/search/Tanek)
  * [http://localhost:7070/user/get/Tanek/id_from_previous_search](http://localhost:7070/user/get/Tanek/id_from_previous_search)

## Finder HTTP Service
Finder comes with a simple microservice that gives you access to the dataset.
```
GET  /:dataset/search/:key  Searches for a given key in the given dataset. Key is prefix matched with the key that's used in the dataset while indexing.
GET  /:dataset/get/:key/:timestamp Returns the underlying record from the Dataset.
```

## More Info
You can define a custom Dataset like
```scala
import finder.spec.Dataset
import finder.util.JSONUtil

case class User(name: String, age: Int, timestamp: Long)

class UserDataset extends Dataset[User] {
  override def key(input: User): String = input.name
  /**
   * Timestamp of the record in UTC timezone
   */
  override def timestamp(input: User): Long = input.timestamp
  override def serialize(input: User): Array[Byte] = JSONUtil.toJson(input).getBytes
  override def deserialize(input: Array[Byte]): User = JSONUtil.readAs(classOf[User])(new String(input))
}
```

All the dataset goes with a configuration in `application.conf` like
```hocon
finder {
  datasets {
    descriptors: [{
      name: "user"
      impl: "finder.models.UserDataset"
      index {
        params {
          db-location: "user-db"
        }
      }
    }]
  }
}
```

Finder uses an Index to store and retrive metadata information regarding every record. By default we use LevelDB backed index engine, but again it is pluggable. Most likely we could use something like [ElephantDB](https://github.com/nathanmarz/elephantdb) to query the index.


## TODOs
- <s>Build a HTTP service to provide access to these datasets</s>
- Write ElephantDB Index Reader
