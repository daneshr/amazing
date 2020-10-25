import React from "react";
import {Form,Button} from 'react-tradeshift-ui';
import JSONInput from 'react-json-editor-ajrm';
import locale    from 'react-json-editor-ajrm/locale/en';
import sampleTree from './structure-sample';


export default class NewStructure extends React.Component {
	constructor(props) {
		 super(props);
		 this.state = { valid:true, data:sampleTree  };
		 this.handleJsonChange = this.handleJsonChange.bind(this);
		 this.handleAction = this.handleAction.bind(this);
	}
	handleJsonChange(event){
		this.setState({valid:!event.error , data:event.json});
	}
	handleAction(){
		if(this.state.valid){
			console.log(this.state.data)
			this.props.action(this.state.data);
		}
		else{
			alert("tree is not valid");
		}
	}
    render() {
        return (
        	<Form>
				<fieldset>
					<span> Enter New Structure </span>
					<label>
						<span>Structure</span>
						<div style={{ maxWidth: "450", maxHeight: "100%" }}>
						<JSONInput
						  onChange={this.handleJsonChange}
				          placeholder={sampleTree} // data to display
				          theme="light_mitsuketa_tribute"
				          locale={locale}
				          colors={{
				            string: "#DAA520" // overrides theme colors with whatever color value you want
				          }}
				          height="450px"
				        />
				        </div>
					</label>
					<Button
						busy={false}
						className="ts-primary"
						type="button"
						onClick={this.handleAction}	>
						  <span>
						    Replace Structure
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
