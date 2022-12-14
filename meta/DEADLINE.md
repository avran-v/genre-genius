# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice looking HTML.

## Part 1: App Description

> Please provide a firendly description of your app, including the app
> category that you chose, and the primary functions available to users
> of the app.

> **Also, include the GitHub `https` URL to your repository.**

I did the External API tool category, using the LastFM and Musixmatch APIs.

Users can choose a genre/category of music they like and receive 3 random lyric snippets from top tracks in that category. From just the snippet, the users guess the track name and artist info, and click a "reveal info" button to see if they were right. Users can choose between different genres, pressing the "shuffle" button for additional or retesting the same one.


## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

I learned a lot more about HTTP requests and formatting them correctly, as well as making the correct classes for Gson to be able to parse my response correctly. It was really fun to use APIs from sites I was familiar with and see them take shape in my app.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

For the genre buttons, I sent out an API request to get top tags (genres/categories), but if I restarted I would take out this step and have some commonly known ones displayed. I felt like I wasted time getting that to work. I would also add functionality for users to search for songs using genres they input into the program.
