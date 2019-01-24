node{

  //Define all variables
  def appName = 'todobackend'
  def imageTag = "mirna/${appName}:${env.BUILD_NUMBER}"
  def dockerFileName = 'Dockerfile-todobackend'
  def containerName = 'todobackend'
  def dbName = 'mydb'
  def dbUserName = 'matthias'
  def dbUserPassword = 'password'
	

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
			credentialsId: 'dockerhub',
			usernameVariable: 'USERNAME',
			passwordVariable: 'PASSWORD']]) {
				docker.withRegistry('', 'dockerhub') {
					sh "docker login -u ${USERNAME} -p ${PASSWORD}"
					sh("docker push ${imageTag}")
				}
			}
		}
    }
	
	
  //Stage 5 : Deploy Application
    stage('Deploy Application on K8s') {
		container('kubectl'){
			withKubeConfig([credentialsId: 'be4d8aa0-a081-466c-a8e9-dc0c151b3654',
		    	serverUrl: 'https://35.198.139.89',
                    	contextName: 'gke_novatec-zweibruecken_europe-west3_nt-asca',
            		clusterName: 'gke_novatec-zweibruecken_europe-west3_nt-asca']){
				sh("kubectl apply -f ${appName}.yml")
				sh("kubectl set image deployment/${appName} ${containerName}=${imageTag}")
				sh("kubectl create configmap postgres-config --from-literal=postgres.db.name=${dbName}")
				sh("kubectl create secret generic db-security --from-literal=db.user.name=${dbUserName} --from-literal=db.user.password=${dbUserPassword}")
				sh("kubectl apply -f postgres.yml")
			}     
		}
	}
}
