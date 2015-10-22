 $(function(){
      var headerScroll = 300;
      $(window).scroll(function() {
      var scroll = getScroll();
      if ( scroll >= headerScroll ) {
        $('.navbar-lp').addClass('scroll');
        }
      else {
        $('.navbar-lp').removeClass('scroll');
        }
});


function getScroll() {
      return window.pageYOffset || document.documentElement.scrollTop;
      }
 });