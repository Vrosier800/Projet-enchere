const ACHATSBTN = document.getElementById('achats');
const VENTESBTN = document.getElementById('ventes');
const ACHATSSELECT = document.getElementById('achats-select');
const VENTESSELECT = document.getElementById('ventes-select');

function desactiverRadio() {
    if (ACHATSBTN.checked) {
        ACHATSSELECT.disabled = false;
        VENTESSELECT.disabled = true;
    } else if (VENTESBTN.checked) {
        ACHATSSELECT.disabled = true;
        VENTESSELECT.disabled = false;
    }
}

ACHATSBTN.addEventListener("change", desactiverRadio);
VENTESBTN.addEventListener("change", desactiverRadio);
