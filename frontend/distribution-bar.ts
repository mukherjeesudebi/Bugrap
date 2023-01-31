import { LitElement, html, customElement, css, property } from 'lit-element';

@customElement('distribution-bar')
export class DistributionBar extends LitElement {
	@property()
	closed = 0;

	@property()
	unresolved = 0;

	@property()
	unassigned = 0;

	static get styles() {
		return css`
		.distributionContainer{
			display:flex;
			flex-direction: row;
		}
		.barStructure{
			min-height:30px;
			min-width:30px;
			color: white;
   			display: flex;
    		justify-content: center;
    		align-items: center;
		}
		.blueBackground{
			background-color:blue;
		}
		.greenBackground{
			background-color:green;
		}
		.orangeBackground{
			background-color:orange;
		}
		`;
	}
	render() {
		return html`
		<div class="distributionContainer">
			<div class="barStructure blueBackground">${this.closed}</div>
			<div class="barStructure greenBackground">${this.unresolved}</div>
			<div class="barStructure orangeBackground">${this.unassigned}</div>
		</div>
		`;
	}
}