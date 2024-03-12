//手机号隐藏
$(".rank-list li").each(function(){
    var phone_num=$(this).find(".rank-list-phone").html().replace(/^(\d{3})\d{6}(\d+)/,"$1******$2");
    $(this).find(".rank-list-phone").html(phone_num)
});



