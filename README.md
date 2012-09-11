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

