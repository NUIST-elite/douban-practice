$("input").bind('input propertychange', function() {
    if($(this).val()!=="") {
        $.get("/searchContent?keyword="+$(this).val(), function(data, status) {
            let allSong = data.songs.content;
            let count = 0;
            if($("ul").children()) {
                $("ul").empty();
            }
            console.log(data);
            for(let song of allSong) {
                console.log("vvv");
                count++;
                if(count > 5) {
                    break;
                }
                var $li= $(`<li>
                            <img src="${song.cover}" alt="">
                            <div class="name">${song.name}</div>
                        </li>`);
                $("ul").append($li);
            }
        })
    }
});