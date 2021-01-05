How to import GoodHotDog database schema?

1. Log in as root
```bash
mysql -u root -p 
```
2. Create database
```mysql
CREATE DATABASE GoodHotDog;
```
3. Leave MySQL terminal
```mysql
exit
```
4. Import schema to created DB
```bash
mysql -u root -p GoodHotDog < GoodHotDog.sql
```