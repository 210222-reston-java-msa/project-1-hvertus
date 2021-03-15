var option;

// define a function for WHAT our ajax call actually does

function ajaxCall() {

	/*//get string placed after equal 
	const option = url.substring(url.indexOf('=') + 1); //last index of a character
	*/
	
	//get url parameters
	const queryString = window.location.search; //should return for example: ?option=profile
	/*const urlParams = new URLSearchParams(queryString);
	const option = urlParams.get('option'); //should return for example: profile*/

    let xhr = new XMLHttpRequest();
    // open the request
    xhr.open("GET", "webapp"+queryString);
    // this is the request to the server: includes the method and the url
        
    // This obj is used for asynchronous requests to a server
    xhr.onreadystatechange = function() {
        if(this.readyState == 4  && this.status == 200) {
        	const response = JSON.parse(xhr.responseText)
            console.log("The response:: "+response);
        }
    }

    // send the request
    xhr.send();
}
