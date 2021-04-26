# telegram-bot
bot name: verlapun_bot

REST:
https://telegrambotv1-311221.ey.r.appspot.com/users  - returns all users
https://telegrambotv1-311221.ey.r.appspot.com/users/531091708 - returns details of concrete User

https://telegrambotv1-311221.ey.r.appspot.com/getmessages  - returns all messages
https://telegrambotv1-311221.ey.r.appspot.com/getmessages?startDate=2021-04-03 00:00:00&endDate=2021-04-09 10:00:00 - returns all messges from the period
https://telegrambotv1-311221.ey.r.appspot.com/getmessages?startDate=2021-04-12 00:00:00 - returns all messages from this date
https://telegrambotv1-311221.ey.r.appspot.com/getmessages?endDate=2021-04-09 10:00:00 - returns all messages before this date


https://telegrambotv1-311221.ey.r.appspot.com/getmessagesforuser?id=531091708 - returns all messages for the User
https://telegrambotv1-311221.ey.r.appspot.com/getmessagesforuser?id=531091708&startDate=2021-04-10 00:00:00 - returns all messages from this date for the User
and other date's combinations


https://telegrambotv1-311221.ey.r.appspot.com/updateHistoryBetweenDates?id=531091708&startDate=2021-04-05 00:00:00&endDate=2021-04-11 00:55:00 - updates history for user,
replaces received meassages to "xxx"
and other date's combinations

https://telegrambotv1-311221.ey.r.appspot.com/deleteUserHistoryBetweenDates?id=860065131&startDate=2021-04-12 00:00:00 &endDate=2021-04-13 00:55:00 - deletes 
history for User
and other date's combinations


To generate Allure report:
allure serve ./target/allure-results/
allure generate ./target/allure-results --clean -o ./target/allure-report 
allure open . 


