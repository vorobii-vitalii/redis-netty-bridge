```shell

printf "*2\r\n\$3\r\nGET\r\n\$13\r\nRATES_CHF_USD\r\n*2\r\n\$3\r\nGET\r\n\$10\r\nRANDOM_123\r\n" | nc localhost 7000

```