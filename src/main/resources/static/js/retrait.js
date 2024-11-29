document.addEventListener('DOMContentLoaded', function() {
	const retraitButton = document.getElementById('retrait');

	retraitButton.addEventListener('click', function(event) {
		event.stopPropagation();
		
		
		let infoRetrait = document.getElementById('donnees')
		let idArticle = infoRetrait.children[0].innerText;
		let pseudoAcquereur = infoRetrait.children[1].innerText;
		let prixFinal = infoRetrait.children[2].innerText;
		let messageAlert = infoRetrait.children[3].innerText;
		
		console.log(idArticle + pseudoAcquereur);
		
		
		alert(`${messageAlert} ${prixFinal} crédit(s) !`);
		window.location.href = `/article/retrait?id=${idArticle}&pseudo=${pseudoAcquereur}`

	});
});