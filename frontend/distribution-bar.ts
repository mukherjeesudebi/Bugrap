import { LitElement, html, customElement, css, property } from 'lit-element';

@customElement('distribution-bar')
export class DistributionBar extends LitElement {
	@property()
	closed = 0;

	@property()
	unResolved = 0;

	@property()
	unAssigned = 0;
	
	@property()
	closedWidth = "";
	
	@property()
	unResolvedWidth = "";
	
	@property()
	unAssignedWidth = "";

	static get styles() {
		return css`
		.distributionContainer{
			display:flex;
			flex-direction: row;
			width: 1000px;
			margin-left: 100px;
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
			<div class="barStructure blueBackground" style="width:${this.closedWidth}">${this.closed}</div>
			<div class="barStructure greenBackground" style="width:${this.unResolvedWidth}">${this.unResolved}</div>
			<div class="barStructure orangeBackground" style="width:${this.unAssignedWidth}">${this.unAssigned}</div>
		</div>
		`;
	}
}