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
	"eDelivery : "true"
}

Update DB with following statement : 

update account_closed Set finalamountpaid=true where phoneno = '6508621001'

Start PAM task and complete it and check if the DB process is done or not.
