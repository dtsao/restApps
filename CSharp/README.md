
<h2>Configuration</h2>
<ul>
<li>visual Studio 12</li>
<li>Windows 7</li>

</ul>
<h2>Implementation</h2>
<h3>Creating the Services</h3>
Creating restful web services in C# is very simple. You can use WCF as template (under File -> New (Project) --> WCF Service Application). With some brief configuration on Web.Config, I was able to default everything to JSON, which is one of the requirements I have for the projects.


<h3>Differences and Challenges</h3>
I tried creating my Person object with an integer for ID, and integer for the url templates (account/{id}), but it seems is required to have strings for those url templates:

<code>
InvalidOperationException: Operation 'GetAPerson' in contract 'IPersonServices' has a path variable named 'id' which does not have type 'string'.  Variables for UriTemplate path segments must have type 'string'.]
</code>
I changed that to use a string - I don't see any other way around it. I left the id attribute as integer on the model, but changed it to string at the service layer.


Also, C# standard is to define attributes of objects with a capital letter.
Ex.: 
<pre>
<code>
   public class Person
    {
        [DataMember(Name="name")]
        public string Name;
    ...
</code>
</pre>

In order to maintain compatibility with the other frameworks, where attributes are defined as lowercase, and keeping my JSON attibutes as lower case, I had to add a <code>(Name="<abc>")</code> to each DataMember I'm exposing. In that way, my test scripts works on both platforms. While before, I had to change my JSON from <code>"name": "Test name"</code> to <code>"Name": "Test Name"</code> every time I run them against C#.


