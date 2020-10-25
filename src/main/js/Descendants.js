import React from "react";
import {Form,Button} from 'react-tradeshift-ui';


export default class Decsendants extends React.Component {
	constructor(props) {
		 super(props);
		 this.state = { nodeId:0};
		 this.handleChange = this.handleChange.bind(this);
		 this.handleAction = this.handleAction.bind(this);
	}
	handleChange(event){
		event.persist();
		this.setState({nodeId:event.target.value});
	}
	handleAction(e){
		e.stopPropagation();
		this.props.action(this.state.nodeId);
	}
    render() {
        return (
        	<Form>
				<fieldset>
					<span> Descendants </span>
					<label>
						<span>Node Id:</span>
						<input onChange={this.handleChange} type="number"/>
					</label>
					<Button
						onClick={this.handleAction}
						busy={false}
						className="ts-primary"
						type="button">
						  <span>
						     Descendants
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