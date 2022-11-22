//Basis Pipeline Schema anlegen

pipeline {
  agent any
  stages {
    stage('Building Feature') {
      when {
              branch 'feature/*'
              beforeAgent true
            }

      //Agent overwrite and run in a docker container
      agent {
        docker {
            image 'gradle:7.5.1-jdk17-focal'
        }
      }


      steps {
        echo 'Building feature'
        sh 'ls -la'
        sh 'gradle build -x test'
      }
    }

    stage('Testing Feature') {
      when {
              branch 'feature/*'
              beforeAgent true
            }

    //Agent overwrite and run in a docker container
      agent {
        docker {
            image 'gradle:7.5.1-jdk17-focal'
        }
      }


      steps {
        echo 'Testing feature'
        sh 'gradle test'
      }

      //post-build
      post {
        always {
            //collect JUnit test  results
            junit 'build/test-results/**/*.xml'
        }
        success {
                    publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'build/reports/tests/test/', reportFiles: 'index.html', reportName: 'HTML Report', reportTitles: ''])
        }
      }

    }

    stage('Integrating Feature') {
      when {
        branch 'feature/*'
        beforeAgent true
      }

      steps {
        echo 'Integrating feature'
      }
    }

  }
}