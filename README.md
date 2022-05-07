# apar-app
An online ordering app where APAR can create, update, and delete products and save them to a Firebase Firestore database.
Users can then create a purchase order based on the products that were added and send it online. 

## Screenshots
<img src="https://github.com/Fangzsx/apar-app/blob/master/app/src/main/assets/Screenshot_20220507-183428_APAR.png?raw=true" width="150" height="280"> <img src="https://github.com/Fangzsx/apar-app/blob/master/app/src/main/assets/Screenshot_20220507-183436_APAR.png?raw=true?" width="150" height="280"> <img src="https://github.com/Fangzsx/apar-app/blob/master/app/src/main/assets/Screenshot_20220507-183440_APAR.png?raw=true" width="150" height="280"> <img src="https://github.com/Fangzsx/apar-app/blob/master/app/src/main/assets/Screenshot_20220507-183451_APAR.png?raw=true" width="150" height="280"> <img src="https://github.com/Fangzsx/apar-app/blob/master/app/src/main/assets/Screenshot_20220507-183543_APAR.png?raw=true" width="150" height="280"> <img src="https://github.com/Fangzsx/apar-app/blob/master/app/src/main/assets/Screenshot_20220507-183611_APAR.png?raw=true" width="150" height="280">


## Vocabularies
1. APAR - Aling Puring Account Representative - a.k.a "Sales Agent" who creates the order for customers and the main user of the app.
2. Old member - Members that already submit their information such as complete name, address, contact info, etc.
3. New Member- Those newly signed up members.

## Difficulties and Inspiration
This app was designed to innovate our process of order taking. The problem that we had was we took orders manually and rely on our memory when the customer ask for checking of the price as well as the total amount purchased. This problem was such a pain so I decided to create an app where we can process the orders using our mobile phones and allow customers to provide their total purchase at the end of our transaction.

## Features and Functionality
1. Create Product - The user can create a product and set its properties such as its name, category, and price
2. Delete Product - If the product was created by accident or no longer exists, the user can delete it
3. Update Product - For wrong inputs and some price adjustments
4. Search Product - For price checking and has the option to delete and update the result
5. Display existing promos in the dashboard - To help APAR in upselling
6. Creating Orders for Old Member and New Member- Total amount can also be seen and informed to the customer right away

## My Project Experience
If I would choose between all my projects, this has to be one of my best one. This was primarily dedicated for my co-workers. For this project, I used the free version of Firebase Firestore for storing user and product informations. I also used Firebase Auth for account creation. Since firebase was working and synching data offline, it it just works perfectly. It took a large amount of time for me to create this app because I was a working student that time and only have limited time to finish this app completely.

## Tech Stack
Kotlin
Firebase Firestore
Firebase Auth

## Run Locally
1. Open Android Studio
2. Click File -> New -> Project from Version Control 
3. In the URL field, paste this: https://github.com/Fangzsx/apar-app.git
4. Set directory of the project
5. Click Clone

## Request Account
To avoid Firebase charges, I only limit access on this project and you can freely ask for demo account by clicking the "Send Request" in Login Activity.
