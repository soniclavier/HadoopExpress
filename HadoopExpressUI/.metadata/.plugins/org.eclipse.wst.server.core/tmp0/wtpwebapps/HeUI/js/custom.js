var toggle = function() {
	var selected = document.getElementById("mlselect").value
	$('#naivebayes').hide("fast")
	$('#logisticreg').hide("fast")
	var selected = "#"+selected;
	$(selected).show("fast")
}

$(".float_quantity").keypress(function (e) {
    // if the letter is not digit then display error and don't type anything
    if (e.which != 46 && e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
              return false;
   }
});

$(".int_quantity").keypress(function (e) {
    // if the letter is not digit then display error and don't type anything
    if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
              return false;
   }
});