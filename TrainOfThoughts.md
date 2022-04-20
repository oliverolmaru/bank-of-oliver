#Database considerations
## Currency Code is both in balance and transactions
- I felt like it would be easier to debug while just glancing at the database table + later volume statistics per currency are easier.
## Annotations vs XML
- Java Annotations based solution felt more familiar at first since MyBatis was unknown to me before this challenge, so I went with that. Looking back and also considering refactor I would move to XML based solution, because from certain POJO size the queries went too long and unreadable in mapper object.


## Request Objects vs directly using database objects
- I usually use directly database objects as much as possible to avoid code duplication, but for better validation and database decoupling (financing has longer lifecycle) I went with explicit request bodies this time

