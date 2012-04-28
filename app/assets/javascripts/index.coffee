$ ->
  $.get "/bars", (data) ->
    $.each data, (index, item) ->
      $("#bars").append $("<li>").text item.name