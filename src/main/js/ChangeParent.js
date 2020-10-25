import React from "react";
import {Form,Button} from 'react-tradeshift-ui';


export default class ChangeParent extends React.Component {
	constructor(props) {
		 super(props);
		 this.state = { nodeId:0, newParentId:0  };
		 this.handleIdChange = this.handleIdChange.bind(this);
		 this.handleParentIdChange = this.handleParentIdChange.bind(this);
		 this.handleAction = this.handleAction.bind(this);
	}
	handleIdChange(event){
		event.persist();
		this.setState(prevState =>{
			   return{
			        ...prevState,
			        nodeId : event.target.value
			   }
		});
		
	}
	handleParentIdChange(event){
		event.persist();
		this.setState(prevState =>{
			   return{
			        ...prevState,
			        newParentId : event.target.value
			   }
		});
	}
	handleAction(e){
		e.stopPropagation();
		this.props.action(this.state.nodeId,this.state.newParentId);
	}
    render() {
        return (
        	<Form>
				<fieldset>
					<span> Change Node Parent </span>
					<label>
						<span>Node Id:</span>
						<input onChange={this.handleIdChange}  type="number"/>
					</label>
					<label>
						<span>New Parent Id:</span>
						<input onChange={this.handleParentIdChange} type="number" />
					</label>
						<Button
						  onClick={this.handleAction}
						  busy={false}
						  className="ts-primary"
						  type="button">
						  <span>
						    Change It!
						  </span>
						</Button>
						<Button
						  onClick={()=>this.props.cancel()}
						  busy={false}
						  className="ts-secondary"
						  type="button">
						  <span>
						    Close
						  </span>
						</Button>
				</fieldset>
			</Form>
        );
    }
}