/**
 * 方法描述:登录首页
 *
 * author 小刘
 * version v1.0
 * date 2015/11/13
 */
$(function(){
    $(document).on('click', '.toolbar a[data-target]', function(e) {
        e.preventDefault();
        var target = $(this).data('target');
        //hide others
        $('.widget-box.visible').removeClass('visible');
        //show target
        $(target).addClass('visible');
    });

    //登录背景切换
    $('#btn-login-dark').on('click', function(e) {
        $('body').attr('class', 'login-layout');
        $('#id-text2').attr('class', 'white');
        $('#id-company-text').attr('class', 'blue');

        e.preventDefault();
    });
    $('#btn-login-light').on('click', function(e) {
        $('body').attr('class', 'login-layout light-login');
        $('#id-text2').attr('class', 'grey');
        $('#id-company-text').attr('class', 'blue');

        e.preventDefault();
    });
    $('#btn-login-blur').on('click', function(e) {
        $('body').attr('class', 'login-layout blur-login');
        $('#id-text2').attr('class', 'white');
        $('#id-company-text').attr('class', 'light-blue');

        e.preventDefault();
    });
});