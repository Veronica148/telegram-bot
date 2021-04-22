# telegram-bot
Cloud run
REST:
http://localhost:8080/sendmessage?textmessage=lower
http://localhost:8080/sendmessagejson?message=vcnvbcnbvcnbvcncyy
User(userId=860065131, firstName=Veronica, lastName=Lapunka)


http://localhost:8080/getusers  - returns all users
http://localhost:8080/getusers?id=531091708 - returns details of concrete User

http://localhost:8080/getmessages  - returns all messages
http://localhost:8080/getmessages?startDate=2021-04-03 00:00:00&endDate=2021-04-09 10:00:00 - returns all messges from the period
http://localhost:8080/getmessages?startDate=2021-04-12 00:00:00 - returns all messages from this date
http://localhost:8080/getmessages?endDate=2021-04-09 10:00:00 - returns all messages before this date


http://localhost:8080/getmessagesforuser?id=531091708 - returns all messages for the User
http://localhost:8080/getmessagesforuser?id=531091708&startDate=2021-04-10 00:00:00 - returns all messages from this date for the User
and other date's combinations


http://localhost:8080/updateHistoryBetweenDates?id=531091708&startDate=2021-04-05 00:00:00&endDate=2021-04-11 00:55:00 - updates history for user,
replaces received meassages to "xxx"
and other date's combinations

http://localhost:8080/deleteUserHistoryBetweenDates?id=860065131&startDate=2021-04-12 00:00:00 &endDate=2021-04-13 00:55:00 - deletes 
history for User
and other date's combinations


To generate Allure report:
allure serve allure-results

For finding processes on the port 8080:
    lsof -n -i4TCP:8080
Kill process by PID:
    kill -9 PID
    
Useful links:
    https://github.com/rubenlagus/TelegramBots/wiki/Getting-Started
    https://www.swtestacademy.com/allure-testng/ 
    
To read:
    https://www.javacodemonk.com/difference-between-getone-and-findbyid-in-spring-data-jpa-3a96c3ff
    https://javarush.ru/groups/posts/2488-obzor-rest-chastjh-3-sozdanie-restful-servisa-na-spring-boot
    https://javarush.ru/groups/posts/2582-dobavljaem-bd-k-restful-servisu-na-spring-boot-chastjh-2
    https://javarush.ru/groups/posts/2579-dobavljaem-bd-k-restful-servisu-na-spring-boot-chastjh-1