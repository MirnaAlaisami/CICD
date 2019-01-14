
docker run --name postgresdb -p 5432:5432 -e POSTGRES_PASSWORD=password -e POSTGRES_USER=matthias -e POSTGRES_DB=mydb -d postgres:latest
