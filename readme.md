# Introduction
Bank of Oliver is the fastest bank you can just download from the web.

# Setup
## Requirements
- Docker
- Docker Compose

## Build & Deploy
1. 

# Scaling application
## Information returned should depend on UI needs
- Currently each parent object returns all data all the way down, which is wrong depending on the scenario. Right now one clear line is between balance and transactions, because one customer has probably few accounts and balances, but lots of transactions. In the future transactions list should be returned in different call.

#Database considerations
## Currency Code is both in balance and transactions
- I felt like it would be easier to debug while just glancing at the database table + later volume statistics per currency are easier.
## Annotations vs XML
- Java Annotations based solution felt more familiar at first since MyBatis was unknown to me before this challenge, so I went with that. Looking back and also considering refactor I would move to XML based solution, because from certain POJO size the queries went too long and unreadable in mapper object.

## Request Objects vs directly using database objects
- I usually use directly database objects as much as possible to avoid code duplication, but for better validation and database decoupling I sparsely used both (db classes and response classes) this time.

# Java Currency object
I would consider using javaCurrency objects instead of currency code strings to pass around data, but since it also is constructed from a string & I don't use it for anything else yet, just went with an enum.