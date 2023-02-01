import { LitElement, html, customElement, css, property } from 'lit-element';

@customElement('report-priority')
export class ReportPriority extends LitElement {
	@property()
	priority = "";

	static get styles() {
		return css`
		.priorityContainer{
			display:flex;
		}
		.priorityColumn{
			height:30px;
			width: 7px;
   			background-color: rgb(10, 139, 174);
    		margin: 2px;
    		border-radius: 7px;
		}
		`;
	}
	render() {		
		return html`<div class="priorityContainer">
		    <div class="priorityColumn" style="display:${this.getDisplay(0,this.priority)};"></div>
			<div class="priorityColumn" style="display:${this.getDisplay(1,this.priority)};"></div>
			<div class="priorityColumn" style="display:${this.getDisplay(2,this.priority)};"></div>
			<div class="priorityColumn" style="display:${this.getDisplay(3,this.priority)};"></div>
			<div class="priorityColumn" style="display:${this.getDisplay(4,this.priority)};"></div>
			<div class="priorityColumn" style="display:${this.getDisplay(5,this.priority)};"></div>
		</div>
		`;
	}
	
	getDisplay(count:number,priority:String){
		var priorityCount = this.getPriorityCount(priority)
		if(count<=priorityCount){
			return "block";
		}else{
			return "none";
		}
	}
	
	getPriorityCount(priority:String){
		if(priority=="BLOCKER"){
			return 5;
		}else if(priority=="CRITICAL"){
			return 4;
		}else if(priority=="MAJOR"){
			return 3;
		}else if(priority=="NORMAL"){
			return 2;
		}else if(priority=="MINOR"){
			return 1;
		}else {
			return 0;
		}
	}
}