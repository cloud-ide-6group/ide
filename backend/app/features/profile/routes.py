from . import profile_bp


@profile_bp.route("/profile", methods=["GET"])
def profile():
    return "profile"
