# Database Setup
1. Install MySQL
2. Create a user with full privileges:
```sql
   CREATE USER 'myuser'@'%' IDENTIFIED WITH caching_sha2_password BY 'your_password';
   GRANT ALL PRIVILEGES ON *.* TO 'myuser'@'%' WITH GRANT OPTION;
   FLUSH PRIVILEGES;
   ```

3.The program can create the database if it does not exist.

For more details about the application and available commands, see the full documentation [here](docs/AxAi%20documentation.pdf).