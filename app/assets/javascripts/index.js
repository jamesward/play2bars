$(function() {
  $.getJSON("/bars", function(data) {
    $("#bars").empty();
    $.each(data, function(index, item) {
      $("#bars").append($("<li>").text(item.name));
    });
  });
});
