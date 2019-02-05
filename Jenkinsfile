node {
	//Define all variables
	def appName = 'todobackend'
	def imageTag = "${env.REPOSITORY}/${appName}:v${env.BUILD_NUMBER}"
	def dockerFileName = 'Dockerfile-todobackend'
	def containerName = 'todobackend'
	
	//Stage 1: Checkout Code from Git
	stage('Application Code Checkout from Git') {
		checkout scm
		
	}
	
	
	//Stage 2: Test Code with Maven/built-in Memory
	stage('Test with Maven/H2') {
		container('maven'){
			dir ("./${appName}") {
				
				sh ("mvn test -Dspring.profiles.active=dev")
				    }
		}
	}
	
	//Stage 3: Test Code with Maven/DB
	stage('Test with Maven/PSQL') {
		container('kubectl'){
			withKubeConfig([credentialsId: env.K8s_CREDENTIALS_ID,
			serverUrl: env.K8s_SERVER_URL,
			contextName: env.K8s_CONTEXT_NAME,
			clusterName: env.K8s_CLUSTER_NAME]){
				sh("kubectl apply -f postgres_test.yml")
			}
				
		}
		container('maven'){
			dir ("./${appName}") {
				
			sh ("mvn test -Dspring.profiles.active=prod -Dspring.datasource.url=jdbc:postgresql://postgres/mydb -Dspring.datasource.username=matthias -Dspring.datasource.password=password")
			}
		}
	}
	
	
	//Stage 4: Build with mvn
	stage('Build with Maven') {
		container('maven'){
			dir ("./${appName}") {
				
				sh ("mvn -B -DskipTests clean package")
			}
		}
	}
	
	

	//Stage 5: Build Docker Image	
	stage('Build Docker Image') {
		container('docker'){
			sh("docker build -f ${dockerFileName} -t ${imageTag} .")
		}
		
	}

	//Stage 6: Push the Image to a Docker Registry
	stage('Push Docker Image to Docker Registry') {
		container('docker'){
			withCredentials([[$class: 'UsernamePasswordMultiBinding',
			credentialsId: env.DOCKER_CREDENTIALS_ID,
			usernameVariable: 'USERNAME',
			passwordVariable: 'PASSWORD']]) {
				docker.withRegistry(env.DOCEKR_REGISTRY, env.DOCKER_CREDENTIALS_ID) {
					sh("docker push ${imageTag}")
				}
			}
		}
		
	}

	//Stage 7: Deploy Application on K8s
	stage('Deploy Application on K8s') {
		container('kubectl'){
			withKubeConfig([credentialsId: env.K8s_CREDENTIALS_ID,
			serverUrl: env.K8s_SERVER_URL,
			contextName: env.K8s_CONTEXT_NAME,
			clusterName: env.K8s_CLUSTER_NAME]){
				sh("kubectl apply -f configmap.yml")
				sh("kubectl apply -f secret.yml")
				sh("kubectl apply -f postgres.yml")
				sh("kubectl apply -f ${appName}.yml")
			}     
		}
        }
	

       
}
