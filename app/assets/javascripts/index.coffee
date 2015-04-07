$ ->
  $.getJSON "/bars", (data) ->
    $("#bars").empty()
    $.each data, (index, item) ->
      $("#bars").append($("<li>").text(item.name))