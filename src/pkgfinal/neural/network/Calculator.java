
package pkgfinal.neural.network;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.ArrayList;

/***
 * @name	Expression Evaluator
 * @author	Jake Wharton
 * @date	2005-11-07
 */
public class Calculator
{
	public static enum EXPRESSIONTYPE
	{
		Prefix, Infix, Postfix
	}
	private static enum OPERATION
	{
		Subtraction, Addition, Modulus, Multiplication, Division, Power, LeftParenthesis, RightParenthesis, Equals
	}
	private static int Precedence(String Operation)
	{
		switch (Operation.charAt(0))
		{
			case '(':
			case ')':
				return 4;
			case '^':
				return 3;
			case '/':
			case 'x':
			case '%':
				return 2;
			case '+':
			case '-':
				return 1;
			default:
				//throw new InvalidArgumentException();
				System.err.println("Invalid operation.");
				return -1;
		}
	}
	private static OPERATION GetOperation(int i)
	{
		switch (i)
		{
			case 0:
				return OPERATION.Subtraction;
			case 1:
				return OPERATION.Addition;
			case 2:
				return OPERATION.Modulus;
			case 3:
				return OPERATION.Multiplication;
			case 4:
				return OPERATION.Division;
			case 5:
				return OPERATION.Power;
			case 6:
				return OPERATION.LeftParenthesis;
			case 7:
				return OPERATION.RightParenthesis;
			default:
				return OPERATION.Equals;
		}
	}
	
	private static String OPERATIONS = "-+%x/^()";
	
	private ExpressionNode Root;
	
	public Calculator(String Expression, EXPRESSIONTYPE Type)
	{
		switch (Type)
		{
			case Prefix:
				this.Root = this.ConstructPrefixExpressionTree(Expression);
				break;
			case Infix:
				this.Root = this.ConstructInfixExpressionTree(Expression);
				break;
			case Postfix:
				this.Root = this.ConstructPostfixExpressionTree(Expression);
				break;
			default:
				System.err.println("Invalid expression type. Reverting to zero.");
				this.Root = new ExpressionNode(0);
		}
	}
	
	private ExpressionNode ConstructPrefixExpressionTree(String PrefixExpression)
	{
		String[] Terms = PrefixExpression.split(" ");
		ArrayList<ExpressionNode> Nodes = new ArrayList<ExpressionNode>();
		
		//create leaf node for each expression term
		for (String Term : Terms)
		{
			if (Calculator.OPERATIONS.indexOf(Term) == -1)
			{
				try
				{
					Nodes.add(new ExpressionNode(Double.valueOf(Term)));
				}
				catch (NumberFormatException e)
				{
					System.err.println("Invalid operand '" + Term + "'. Reverting to zero.");
					Nodes.add(new ExpressionNode(0));
				}
			}
			else
			{
				Nodes.add(new ExpressionNode(Calculator.GetOperation(Calculator.OPERATIONS.indexOf(Term))));
			}
		}
		
		try
		{
			int Current = Nodes.size() - 3; //first possible operation position
			while (Nodes.size() > 1)
			{
				if (Nodes.get(Current).IsOperation())
				{
					//assign children and remove them from working nodes
					Nodes.get(Current).SetChildren(Nodes.remove(Current + 1), Nodes.remove(Current + 1));
					Current = Nodes.size() - 3; //reset to first possible position
				}
				else
				{
					--Current;
				}
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			System.err.println("Too many/few operations. Reverting expression to zero.");
			Nodes.clear();
			Nodes.add(new ExpressionNode(0));
		}
		catch (Exception e)
		{
			System.err.println(e);
			System.err.println("Reverting expression to zero.");
			Nodes.clear();
			Nodes.add(new ExpressionNode(0));
		}
		
		//last node, root node
		return Nodes.get(0);
	}
	private ExpressionNode ConstructInfixExpressionTree(String InfixExpression)
	{
		//TODO: unary operators
		
		//un-suck infix with spacing and check parenthesis
		int j = 0;
		for (int i = 0; i < InfixExpression.length(); ++i)
		{
			if (Calculator.OPERATIONS.indexOf(InfixExpression.charAt(i)) != -1)
			{
				if (InfixExpression.charAt(i) == '(') ++j;
				if (InfixExpression.charAt(i) == ')') --j;
				if (j < 0)
				{
					System.err.println("Opening parenthesis expected. Reverting expression to zero.");
					return new ExpressionNode(0);
					//throw new Exception... Mismatched parenthesis at character i
				}
				if ((i > 0) && (InfixExpression.charAt(i - 1) != ' '))
				{
					InfixExpression = InfixExpression.substring(0, i) + " " + InfixExpression.substring(i);
					++i; //adjust for insertion
				}
				if ((i < InfixExpression.length() - 1) && (InfixExpression.charAt(i + 1) != ' '))
				{
					InfixExpression = InfixExpression.substring(0, i + 1) + " " + InfixExpression.substring(i + 1);
					++i; //adjust for insertion
				}
			}
		}
		if (j > 0)
		{
			System.err.println("Closing parenthesis expected. Reverting expression to zero.");
			return new ExpressionNode(0);
			//throw new Exception... Mismatched parenthesis
		}
		
		String[] Terms = InfixExpression.split(" ");
		ArrayList<ExpressionNode> Nodes = new ArrayList<ExpressionNode>();
		
		//create leaf node for each expression term
		for (String Term : Terms)
		{
			if (Calculator.OPERATIONS.indexOf(Term) == -1)
			{
				try
				{
					Nodes.add(new ExpressionNode(Double.valueOf(Term)));
				}
				catch (NumberFormatException e)
				{
					System.err.println("Invalid operand '" + Term + "'. Reverting to zero.");
					Nodes.add(new ExpressionNode(0));
				}
			}
			else
			{
				Nodes.add(new ExpressionNode(Calculator.GetOperation(Calculator.OPERATIONS.indexOf(Term))));
			}
		}
		
		//temporary stack space
		ArrayList<ExpressionNode> OperationStack = new ArrayList<ExpressionNode>();
		ArrayList<ExpressionNode> ValueStack = new ArrayList<ExpressionNode>();
		
		try
		{
			//iterate terms and construct tree
			for (int i = 0; i < Nodes.size(); ++i)
			{
				switch (Nodes.get(i).toString().charAt(0))
				{
					case '(':
						OperationStack.add(0, Nodes.get(i));
						break;
					case ')':
						//assign operations children until left parenthesis is encountered
						while (OperationStack.get(0).toString().equals("(") == false)
						{
							ValueStack.add(0, OperationStack.remove(0));
							ValueStack.get(0).SetChildren(ValueStack.remove(2), ValueStack.remove(1));
						}
						OperationStack.remove(0); //remove left parenthesis
						break;
					case '-':
					case '+':
					case '%':
					case 'x':
					case '/':
					case '^':
						//assign operations children until operation stack is empty, a left parenthesis is encountered, or a lower precedence operation is encountered
						while ((OperationStack.size() > 0) && (OperationStack.get(0).toString().equals("(") == false) && (Calculator.Precedence(Nodes.get(i).toString()) <= Calculator.Precedence(OperationStack.get(0).toString())))
						{
							ValueStack.add(0, OperationStack.remove(0));
							ValueStack.get(0).SetChildren(ValueStack.remove(2), ValueStack.remove(1));
						}
						OperationStack.add(0, Nodes.get(i));
						break;
					default: //case number:
						ValueStack.add(0, Nodes.get(i));
				}
			}
			//assign the rest of the operations children
			while (OperationStack.size() > 0)
			{
				ValueStack.add(0, OperationStack.remove(0));
				ValueStack.get(0).SetChildren(ValueStack.remove(2), ValueStack.remove(1));
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			System.err.println("Too many/few operations. Reverting expression to zero.");
			ValueStack.clear();
			ValueStack.add(new ExpressionNode(0));
		}
		catch (Exception e)
		{
			System.err.println(e);
			System.err.println("Reverting expression to zero.");
			ValueStack.clear();
			ValueStack.add(new ExpressionNode(0));
		}
		
		//last node, root node
		return ValueStack.get(0);
	}
	private ExpressionNode ConstructPostfixExpressionTree(String PostfixExpression)
	{
		String[] Terms = PostfixExpression.split(" ");
		ArrayList<ExpressionNode> Nodes = new ArrayList<ExpressionNode>();
		
		//create a leaf node for each expression term
		for (String Term : Terms)
		{
			if (Calculator.OPERATIONS.indexOf(Term) == -1)
			{
				try
				{
					Nodes.add(new ExpressionNode(Double.valueOf(Term)));
				}
				catch (NumberFormatException e)
				{
					System.err.println("Invalid operand '" + Term + "'. Reverting to zero.");
					Nodes.add(new ExpressionNode(0));
				}
			}
			else
			{
				Nodes.add(new ExpressionNode(Calculator.GetOperation(Calculator.OPERATIONS.indexOf(Term))));
			}
		}
		
		try
		{
			int Current = 2; //first possible operation postition
			while (Nodes.size() > 1)
			{
				if (Nodes.get(Current).IsOperation())
				{
					//assign children and remove them from working nodes
					Nodes.get(Current).SetChildren(Nodes.remove(Current - 2), Nodes.remove(Current - 2));
					Current = 2; //reset to first possible position
				}
				else
				{
					++Current;
				}
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			System.err.println("Too many/few operations. Reverting expression to zero.");
			Nodes.clear();
			Nodes.add(new ExpressionNode(0));
		}
		catch (Exception e)
		{
			System.err.println(e);
			System.err.println("Reverting expression to zero.");
			Nodes.clear();
			Nodes.add(new ExpressionNode(0));
		}
		
		//last node, root node
		return Nodes.get(0);
	}
	
	public String GetPrefixExpression()
	{
		return this.Root.GetPrefixExpression();
	}
	public String GetInfixExpression()
	{
		return this.Root.GetInfixExpression();
	}
	public String GetPostfixExpression()
	{
		return this.Root.GetPostfixExpression();
	}
	public double GetValue()
	{
		return this.Root.GetValue();
	}
	
	private class ExpressionNode
	{
		private double Value;
		private OPERATION Operation;
		private ExpressionNode Left;
		private ExpressionNode Right;
		
		public ExpressionNode(double Value)
		{
			this.Value = Value;
			this.Operation = OPERATION.Equals;
			this.Left = null;
			this.Right = null;
		}
		public ExpressionNode(OPERATION Operation)
		{
			this(Operation, null, null);
		}
		public ExpressionNode(OPERATION Operation, ExpressionNode Left, ExpressionNode Right)
		{
			this.Value = 0;
			if (Operation == OPERATION.Equals)
			{
				System.err.println("You cannot explicitly define an Equals node.");
				System.exit(1);
				//throw new Exception("You cannot explicitly define an Equals node. To define a value-only node, passing in a double");
			}
			this.Operation = Operation;
			this.Left = Left;
			this.Right = Right;
		}
		
		public void SetChildren(ExpressionNode Left, ExpressionNode Right)
		{
			this.Left = Left;
			this.Right = Right;
		}
		public double GetValue()
		{
			//determine and perform operation on children
			switch (this.Operation)
			{
				case Equals:
					break;
				case Power:
					this.Value = Math.pow(this.Left.GetValue(), this.Right.GetValue());
					break;
				case Division:
					this.Value = this.Left.GetValue() / this.Right.GetValue();
					break;
				case Multiplication:
					this.Value = this.Left.GetValue() * this.Right.GetValue();
					break;
				case Modulus:
					this.Value = (int)this.Left.GetValue() % (int)this.Right.GetValue();
					break;
				case Addition:
					this.Value = this.Left.GetValue() + this.Right.GetValue();
					break;
				case Subtraction:
					this.Value = this.Left.GetValue() - this.Right.GetValue();
					break;
				default:
					this.Value = 0;
					//error
			}
			return this.Value;
		}
		private String GetOperation()
		{
			return String.valueOf(Calculator.OPERATIONS.charAt(this.Operation.ordinal()));
		}
		public String GetPrefixExpression()
		{
			if (this.Operation == OPERATION.Equals)
			{
				return String.valueOf(this.Value);
			}
			else
			{
				return this.GetOperation() + " " + this.Left.GetPrefixExpression() + " " + this.Right.GetPrefixExpression();
			}
		}
		public String GetInfixExpression()
		{
			if (this.Operation == OPERATION.Equals)
			{
				return String.valueOf(this.Value);
			}
			else
			{
				//fully parenthesized
				return "(" + this.Left.GetInfixExpression()  + " " + this.GetOperation() + " " + this.Right.GetInfixExpression() + ")";
			}
		}
		public String GetPostfixExpression()
		{
			if (this.Operation == OPERATION.Equals)
			{
				return String.valueOf(this.Value);
			}
			else
			{
				return this.Left.GetPostfixExpression() + " " + this.Right.GetPostfixExpression() + " " + this.GetOperation();
			}
		}
		public boolean IsOperation()
		{
			//if the node is not a value and the children are not assigned then we are a stand-alone operation
			return (this.Operation != OPERATION.Equals) && (this.Left == null);
		}
		public String toString()
		{
			return (this.Operation != OPERATION.Equals) ? this.GetOperation() : String.valueOf(this.Value);
		}
	}
}