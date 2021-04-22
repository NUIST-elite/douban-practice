$(".is-like").click(function() {
    $(this).prev().prev().prev().fadeIn();
    let url = $(this).prev().prev().attr("src");
    $(".my-like").each(function() {
        if($(this).attr("src") === url) {
            return false;
        }
        if($(this).attr("src") === "./css/pic/userguide-pic/general.png") {
            $(this).attr("src", url);
            $(this).prev().css("display", "block");
            return false;
        }
    });

    let isTrue = true;
    $(".my-like").each(function() {
        if($(this).attr("src") === "./css/pic/userguide-pic/general.png") {
            isTrue = false;
        }
    });

    if(isTrue) {
        $(".title").hide();
        $(".singer").hide();
        $(".move").hide();
        $(".skip").hide();
        $(".login").hide();
        $(".like").css("margin-top", "345px");
        $(".my-Mhz").css("display", "flex");
    }
});

$(".small-red").hover(
    function() {
        $(this).children().css("display", "block");
    },
    function() {
        $(this).children().css("display", "none");
    });

$(".reload").click(function() {
    axios.get("http://localhost:8080/singer/random")
        .then(function(response) {
            let count = 0;
            $(".ten-singer").each(function() {
                $(this).find("img").attr("src", response.data[count].avatar);
                $(this).find(".name").text(response.data[count].name);
                count++;
            });
        });
});

$(".my-like-singers").click(function() {
    if($(this).find(".my-like").attr("src") !== "./css/pic/userguide-pic/general.png") {
        let url = $(this).find(".my-like").attr("src");
        $(this).find(".my-like").attr("src", "./css/pic/userguide-pic/general.png");
        $(this).find(".small-red").css("display", "none");
        $(".ten-singer").each(function() {
            if($(this).find("img").attr("src") === url) {
                $(this).find(".red").css("display", "none");
                return false;
            }
        });
    }
});

