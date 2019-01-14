kubectl get svc postgres-cip -o jsonpath="{.spec.clusterIP}"
kubectl expose deployment postgresdb --type=ClusterIP --name=postgres-cip --port 5432
kubectl expose deployment postgresdb --type=LoadBalancer --name=postgres-lb --port 5432
kubectl run postgresdb --replicas=1 --image=postgres:latest --env="POSTGRES_PASSWORD=password" --env="POSTGRES_USER=matthias" --env="POSTGRES_DB=mydb"
