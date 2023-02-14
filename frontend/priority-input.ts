import { LitElement, html, customElement, css, property } from 'lit-element';

@customElement('priority-input')
export class PriorityInput extends LitElement {
	@property()
	priority = "";

	static get styles() {
		return css`
		.priority-container{
			display:flex;
			flex-direction:column;
			width:100%
		}
		.priority-labels{
			display:flex;
			flex-direction:row;
			font-size: 12px;
		}
		.priority-labels > div{
			width:85px;
			display: flex;
   			flex-direction: row;
    		justify-content: flex-start;
    		align-items: center;
		}
		.priority-action{
			display:flex;
			flex-direction:row;
			height: 10px;
			width: 100%
		}
		.priority-action > div{
			width: 100px;
			border: 1px solid #d3d1d1;
			background-color: #e2dede;
		}
		.priority-action div:hover:nth-child(1){
			background-color: green;
		}
		`;
	}
	render() {
		return html`
		<div class="priority-container">
			<div class="priority-labels">
				<vaadin-radio-group theme="horizontal">
					<vaadin-radio-button value="TRIVIAL" label="TRIVIAL" checked></vaadin-radio-button>
					<vaadin-radio-button value="MINOR" label="MINOR"></vaadin-radio-button>
					<vaadin-radio-button value="NORMAL" label="NORMAL"></vaadin-radio-button>
					<vaadin-radio-button value="MAJOR" label="MAJOR"></vaadin-radio-button>
					<vaadin-radio-button value="CRITICAL" label="CRITICAL"></vaadin-radio-button>
					<vaadin-radio-button value="BLOCKER" label="BLOCKER"></vaadin-radio-button>
				</vaadin-radio-group>
			</div>
			<div class="priority-action">
				<div></div>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
				<div></div>
			</div>
		</div>
		
		`;
	}

}