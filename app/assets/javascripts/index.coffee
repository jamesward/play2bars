$ ->
  $.get "/listBars", (data) ->
    $.each data, (index, item) ->
      $("#bars").append "<li>Bar " + item.name + "</li>"