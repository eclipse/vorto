Add a new blog post to Vorto
==================================

Welcome to Vorto's blogging space!

Prerequisites
-------------

 - [Markdown] Knowledge 

 
Steps:
------
  * git clone [https://github.com/eclipse/vorto.git](https://github.com/eclipse/vorto.git)
  * Create a new markdown file under **vorto\docs\blog**
  * Save the md file in the following syntax
	  * _yyyy-mm-dd-filename.md_
  * Start adding blog contents in the _markdown format_
  * Prefix the blog contents with **YAML front matter block** as given below:
  
  		---
		layout: blog
		title: <a suitable title>
		date:   yyyy-mm-dd
		section: blog
		author: <Name of the author>
		authorid: <user id if any>
		---
  * Store the image files under **vorto\docs\img\blogpics**
  * Prefix the path of the image files with **{{base}}**
	 - _e.g_  {{base}}/img/blogpics/sampleimage.png


Support
-------
For more details and documentation, 
visit 

[Vorto Home](http://www.eclipse.org/vorto)

[Vorto Discussions/Community](http://www.eclipse.org/vorto/community.html) 


[Markdown]:  https://guides.github.com/features/mastering-markdown/
