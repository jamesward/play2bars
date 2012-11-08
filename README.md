Deploy on Heroku
----------------

1. [Install the Heroku Toolbelt](http://toolbelt.heroku.com)

2. [Signup for a Heroku Account](http://heroku.com/signup)

3. Login to Heroku:

        $ heroku login

4. Create a new Heroku app:

        $ heroku create

5. Add the MongoHQ Add-on

        $ heroku addons:add mongohq

6. Push the local repo to Heroku:

        $ git push heroku scala-mongodb:master

7. Open the app:

        $ heroku open


Local Setup (OS X Specific)
---------------------------

1. [Download MongoDB](http://www.mongodb.org/downloads)

2. Extract MongoDB:

		tar -zxvf mongo.tgz

3. Create a directory for data (personal preference is under the MongoDB directory):

		sudo mkdir -p /apps/mongodb-osx-x86_64-2.2.1/data/db 
   
   If you create the data directory in the root file system update the permissions:

   		sudo chown `id -u` data/db

4. Add the MongoDB bin directory to the system path.  Modify ~/.bash_profile and add the following (customize for where you extracted MongoDB):

		export PATH=/apps/mongodb-osx-x86_64-2.2.1/bin:$PATH

5. Run MongoDB:

		mongod --dpath=/apps/mongodb-osx-x86_64-2.2.1/data/db/

6. Run the play application - in the play2bars directory run:

		play run 

7. You can query mongo to see the bars entered in the db.  At a terminal prompt enter:

		mongo
		use play2bars
		db.bars.find()
		db.bars.count()

