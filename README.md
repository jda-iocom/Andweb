# Andweb
Sample showing how to embed a WebViewClient in an Android app, and connect to the Javascript on the page.

In order for the embedded Javascript to access methods in the Android Java, you need to add the @Javascript tag. For example:


    @Override
    @JavascriptInterface
    public String GetName() {
        return "Test User";
    }

In the Javascript, you can get at the @JavascriptInterface methods by referencing the object as named with the addJavascriptInteface
method:

        wv.addJavascriptInterface(this,"plugin");

So, to call the above method in your Javascript, on the web page, just use the method on the '''plugin''' object:

    username = plugin.GetName()
    
