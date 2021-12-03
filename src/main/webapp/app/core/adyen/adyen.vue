<template>
    <div id=app>
      <form>
        <fieldset>
          <legend>Informations CB</legend>
          <ol>
            <li>
              <fieldset>
                <legend>Type de carte bancaire</legend>
                <ol>
                  <li>
                    <input id=visa name=type_de_carte type=radio>
                    <label for=visa>VISA</label>
                  </li>
                  <li>
                    <input id=mastercard name=type_de_carte type=radio>
                    <label for=mastercard>Mastercard</label>
                  </li>
                </ol>
              </fieldset>
            </li>
            <li>
              <label for=numero_de_carte>N° de carte</label>
              <input type=number name=cardnumber id=cardnumber required>
            </li>
            <li>
                <label for="expiration">Date d'expiration :</label>
                <input type="month" name=expirationdate id=expirationdate required>
              </li>
            <li>
              <label for=securite>Code de sécurité</label>
              <input type=number name=securite id=securite placeholder="4 chiffres" required>
            </li>
            <li>
              <label for=nom_porteur>Nom du porteur</label>
              <input type=text name=name id=cardname placeholder="Nom indiqué sur la carte" required>
            </li>
          </ol>
        </fieldset>
        <fieldset>
          <button type="submit">Payer</button>
        </fieldset>
      </form>
      </div>
</template>

<script lang="js" src="./adyen.js"></script>
