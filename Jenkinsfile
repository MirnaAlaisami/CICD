node {
	//Define all variables
	def app1_name = 'todobackend'
	def app2_name = 'todoui'
	def app1_image_tag = "${env.REPOSITORY}/${app1_name}:v${env.BUILD_NUMBER}"
	def app2_image_tag = "${env.REPOSITORY}/${app2_name}:v${env.BUILD_NUMBER}"
	def app1_dockerfile_name = 'Dockerfile-todobackend'
	def app2_dockerfile_name = 'Dockerfile-todoui'
	def app1_container_name = 'todobackend'
	def app2_container_name = 'todoui'
	
	//Stage 1: Checkout Code from Git
	stage('Application Code Checkout from Git') {
		checkout scm
		
	}
	
	
	
	//Stage 2: Test Code with Maven/built-in Memory
	stage('Test with Maven/H2') {
		container('maven'){
			dir ("./${app1_name}") {
				
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
			dir ("./${app1_name}") {
				sh ("mvn test -Dspring.profiles.active=prod -Dspring.datasource.url=jdbc:postgresql://${env.PSQL_TEST}/${env.DB_NAME} -Dspring.datasource.username=${env.DB_USERNAME} -Dspring.datasource.password=${env.DB_PASSWORD}")
			}
		}
	}
	
	
	
	
	//Stage 4: Build with mvn
	stage('Build with Maven') {
		container('maven'){
			dir ("./${app1_name}") {
				
				sh ("mvn -B -DskipTests clean package")
			}
			dir ("./${app2_name}") {
				
				sh ("mvn -B -DskipTests clean package")
			}
		}
	}
	

	//Stage 5: Build Docker Image	
	stage('Build Docker Image') {
		container('docker'){
			sh("docker build -f ${app1_dockerfile_name} -t ${app1_image_tag} .")
			sh("docker build -f ${app2_dockerfile_name} -t ${app2_image_tag} .")
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
					sh("docker push ${app1_image_tag}")
					sh("docker push ${app2_image_tag}")
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
				sh("kubectl apply -f ${app1_name}.yml")
				sh("kubectl set image deployment/${app1_name} ${app1_container_name}=${app1_image_tag}")
				sh("kubectl apply -f ${app2_name}.yml")
				sh("kubectl set image deployment/${app2_name} ${app2_container_name}=${app2_image_tag}")
			}     
		}
        }
  
}


