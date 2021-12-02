<template>
  <div id="paypal-button-container"></div>
</template>

<script>
export default {
  mounted() {
    let paypalscript = document.createElement('script');
    paypalscript.setAttribute('src', './paypal.js');
    document.head.appendChild(paypalscript);

    let paypalscriptinternet = document.createElement('script');
    paypalscriptinternet.setAttribute(
      'src',
      'https://www.paypal.com/sdk/js?client-id=AZyVvavpkhKOdesqvT8jWNnYsgsZ81UiBvTND4swJEs9oT4AjXU-biqVsTC657gHpxWFcdzX43BFsem1'
    );
    document.head.appendChild(paypalscriptinternet);
  },
};
</script>

<script src="https://www.paypal.com/sdk/js?client-id=AZyVvavpkhKOdesqvT8jWNnYsgsZ81UiBvTND4swJEs9oT4AjXU-biqVsTC657gHpxWFcdzX43BFsem1"></script>
<script src="./paypal.js"></script>
