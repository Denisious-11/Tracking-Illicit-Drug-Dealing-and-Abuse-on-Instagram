import re

text = "hey amazon - my package never arrived https://www.amazon.com/gp/css/order-history?ref_=nav_orders_first please fix asap! @amazonhelp #delhi"
text = re.sub(r"(?:\@|https?\://)\S+", "", text)
text=re.sub(r"#(\w+)", "",text)

print(text)