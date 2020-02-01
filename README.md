Play 2 Bars - ScalikeJDBC Async
-------------------------------

Run Locally (requires Docker):

```
docker run --rm --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=password postgres
docker exec -i postgres psql -U postgres < conf/db/default/schema.sql
./sbt ~run
```

Visit: [http://localhost:9000](http://localhost:9000)


# TODO
- TestContainers
- Evolutions
