var xhr = new XMLHttpRequest();
var url = "http://localhost:8080/paiement";

var cardnumber = "1122334455667788";
var expirationdate = "2042";
var securite = "321";
var cardname = "yowan";

xhr.open("POST", url, true);
xhr.setRequestHeader("Content-Type", "application/json");
xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
        var json = JSON.parse(xhr.responseText);
        console.log(json.email + ", " + json.password);
    }
};
var data = JSON.stringify({"cardnumber": cardnumber, "expirationdate": expirationdate, "securite": securite, "cardname": cardname});
xhr.send(data);