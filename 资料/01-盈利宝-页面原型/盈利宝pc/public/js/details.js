//手机号隐藏
$(".datail-record-list tbody tr").each(function(){
    var phone_num=$(this).find(".datail-record-phone").html().replace(/^(\d{3})\d{6}(\d+)/,"$1******$2");
    $(this).find(".datail-record-phone").html(phone_num)
});















