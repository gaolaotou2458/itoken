echo "Hello bat"
cd ..
cd itoken-common
call mvn clean compile package deploy

cd ..
cd itoken-common-domain
call mvn clean compile package deploy

cd ..
cd itoken-common-service
call mvn clean compile package deploy

cd ..
cd itoken-common-web
call mvn clean compile package deploy

cd ..