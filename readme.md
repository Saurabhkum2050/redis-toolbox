# RedisToolbox

RedisToolbox is a scala library for dealing with redis data.

## Installation

Use scala package manager [sbt](https://www.scala-sbt.org/download) to generate RedisToolbox.jar

```bash
sbt clean compile assembly
```

## Usage (Java)

```bash
java -jar \
-DSOURCE_HOST='localhost' \
-DSOURCE_PORT=6379 \
-DSOURCE_SECRET='shanky' \
-DSOURCE_DB=0 \
-DTARGET_HOST='localhost' \
-DTARGET_PORT=6379 \
-DTARGET_SECRET='shanky' \
-DTARGET_DB=1 \
./target/scala-2.13/RedisToolBox.jar
```


## Usage (Docker)

```bash
docker run --rm -it \
-e SOURCE_HOST=192.168.0.222 \
-e SOURCE_PORT=6379 \
-e SOURCE_SECRET=shanky \
-e SOURCE_DB=0 \
-e TARGET_HOST=192.168.0.222 \
-e TARGET_PORT=6379 \
-e TARGET_SECRET=shanky \
-e TARGET_DB=1 \
--name RedisToolBox \
redis-toolbox:test
```



## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

N/A