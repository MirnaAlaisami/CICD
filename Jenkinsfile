node {
	//Define all variables
	def appName = 'todobackend'
	def imageTag = "${env.Docker_Registry}/${appName}:v${env.BUILD_NUMBER}"
	def dockerFileName = 'Dockerfile-todobackend'
	def containerName = 'todobackend'
	
	//Stage 1: Checkout Code from Git
	stage('Application Code Checkout from Git') {
		checkout scm
	}

  //Stage 2: Build with mvn
	stage('Build with Maven') {
		container('maven'){
			dir ("./${appName}") {
				sh ("mvn -B -DskipTests clean package")
			}
		}
	}

	//Stage 3: Build Docker Image	
	stage('Build Docker Image') {
		container('docker'){
			sh("docker build -f ${dockerFileName} -t ${imageTag} .")
		}
		
	}

	//Stage 4: Push the Image to a Docker Registry
	stage('Push Docker Image to Docker Registry') {
		container('docker'){
			withCredentials([[$class: 'UsernamePasswordMultiBinding',
			credentialsId: env.Docker_Credentials_ID,
			usernameVariable: 'USERNAME',
			passwordVariable: 'PASSWORD']]) {
				docker.withRegistry(env.Docker_Registry, env.Docker_Credentials_ID) {
					sh("docker push ${imageTag}")
				}
			}
		}
		
	}

	//Stage 5 : Deploy Application on K8s
	stage('Deploy Application on K8s') {
		container('kubectl'){
			withKubeConfig([credentialsId: env.K8s_Credentials_ID,
			serverUrl: env.K8s_ServerURL,
			contextName: env.K8s_ContextName,
			clusterName: env.K8s_ClusterName]){
				sh("kubectl apply -f configmap.yml")
				sh("kubectl apply -f secret.yml")
				sh("kubectl apply -f postgres.yml")
				sh("kubectl apply -f ${appName}.yml")
			}     
		}
		
    }
	   
}
