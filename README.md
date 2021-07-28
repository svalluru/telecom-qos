Happy Path : 

Put Below JSON message in close-account topic

{
	"phoneno" : "6508621000",
	"unlock" : "false",
	"eDelivery" : "true"
}

Update DB with following statement : 

update account_closed Set finalamountpaid=true where phoneno = '6508621000'


Unlock devide failed Path : 

Put Below JSON message in close-account topic

{
	"phoneno" : "6508621001",
	"unlock" : "true",
	"eDelivery" : "true"
}

Update DB with following statement : 

update account_closed Set finalamountpaid=true where phoneno = '6508621001'

Start PAM task and complete it and check if the DB process is done or not.




CREATE TABLE account_closed (
    phoneno varchar(255),
    unlockphone boolean,
    phoneunlocked boolean,
    eDelivery boolean,
    finalamount varchar(255),
    finalamountpaid boolean,
    accountclosed boolean,
    finalbillnotificationsent boolean,
    daterequested varchar(255),
    status varchar
);

docker run -p 3306:3306 --name=sri-mysql --env="MYSQL_ROOT_PASSWORD=mypassword" mysql

Start Jboss EAP standalone.sh -c standalone-full.xml
PAM console http://localhost:8080/business-central/kie-wb.jsp